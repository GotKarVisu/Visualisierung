#version 150
#extension GL_ARB_shading_language_420pack : require
#extension GL_ARB_explicit_attrib_location : require

in vec3 ray_entry_position;
layout(location = 0) out vec4 FragColor;

uniform mat4 Modelview;

uniform sampler3D volume_texture;
uniform sampler2D transfer_texture;

uniform vec3    camera_location;
uniform float   sampling_distance;
uniform float   iso_value;
uniform vec3    max_bounds;
uniform ivec3   volume_dimensions;

uniform vec3    light_position;
uniform vec3    light_color;

struct material {
    vec4 ambient;
    vec4 diffus;
    vec4 specular;
    float shine;
};

bool
inside_volume_bounds(const in vec3 sampling_position)
{
    return (   all(greaterThanEqual(sampling_position, vec3(0.0)))
            && all(lessThanEqual(sampling_position, max_bounds)));
}

float
get_nearest_neighbour_sample(vec3 in_sampling_pos){
    
    vec3 obj_to_tex                 = vec3(1.0) / max_bounds;
    
    /// transform from texture space to array space
    /// ie: (0.3, 0.5, 1.0) -> (76.5 127.5 255.0)
    vec3 sampling_pos_array_space_f = in_sampling_pos * vec3(volume_dimensions);


    // this time we just round the transformed coordinates to their next integer neighbors
    // i.e. nearest neighbor filtering
    vec3 interpol_sampling_pos_f;
    interpol_sampling_pos_f.x = round(sampling_pos_array_space_f.x);
    interpol_sampling_pos_f.y = round(sampling_pos_array_space_f.y);
    interpol_sampling_pos_f.z = round(sampling_pos_array_space_f.z);
        

    /// transform from array space to texture space
    vec3 sampling_pos_texture_space_f = interpol_sampling_pos_f/vec3(volume_dimensions);

    // access the volume data
    return texture(volume_texture, sampling_pos_texture_space_f * obj_to_tex).r;
}

float
get_triliniear_sample(vec3 in_sampling_pos){
    
	vec3	boundary_voxel[8];
    vec3 obj_to_tex                 = vec3(1.0) / max_bounds;
    
    /// transform from texture space to array space
    /// ie: (0.3, 0.5, 1.0) -> (76.5 127.5 255.0)
    vec3 sampling_pos_array_space_f = in_sampling_pos * vec3(volume_dimensions);

	vec3 floor_pos;
	floor_pos.x = floor(sampling_pos_array_space_f.x);
	floor_pos.y = floor(sampling_pos_array_space_f.y);
	floor_pos.z = floor(sampling_pos_array_space_f.z);

	float size = 1;

	boundary_voxel[0].x = floor_pos.x;
	boundary_voxel[0].y = floor_pos.y;
	boundary_voxel[0].z = floor_pos.z;

	boundary_voxel[1].x = floor_pos.x+size;
	boundary_voxel[1].y = floor_pos.y;
	boundary_voxel[1].z = floor_pos.z;

	boundary_voxel[2].x = floor_pos.x;
	boundary_voxel[2].y = floor_pos.y+size;
	boundary_voxel[2].z = floor_pos.z;

	boundary_voxel[3].x = floor_pos.x;
	boundary_voxel[3].y = floor_pos.y;
	boundary_voxel[3].z = floor_pos.z+size;

	boundary_voxel[4].x = floor_pos.x+size;
	boundary_voxel[4].y = floor_pos.y;
	boundary_voxel[4].z = floor_pos.z+size;

	boundary_voxel[5].x = floor_pos.x;
	boundary_voxel[5].y = floor_pos.y+size;
	boundary_voxel[5].z = floor_pos.z+size;

	boundary_voxel[6].x = floor_pos.x+size;
	boundary_voxel[6].y = floor_pos.y+size;
	boundary_voxel[6].z = floor_pos.z;

	boundary_voxel[7].x = floor_pos.x+size;
	boundary_voxel[7].y = floor_pos.y+size;
	boundary_voxel[7].z = floor_pos.z+size;

	float u = distance(sampling_pos_array_space_f.x, floor_pos.x);
	float v = distance(sampling_pos_array_space_f.y, floor_pos.y);
	float w = distance(sampling_pos_array_space_f.z, floor_pos.z);
	// this time we just round the transformed coordinates to their next integer neighbors
    // i.e. nearest neighbor filtering
    vec3 interpol_sampling_pos_f;
	
	// 0 - 1 - 2 - 6 front = z
	// 3 - 4 - 5 - 7 back  = z+1
	// front - back
	
	vec3 front;
	vec3 back;
	
	front = (1-v)*((1-u)*boundary_voxel[0] + u * boundary_voxel[1]) + v * ((1-u)*boundary_voxel[2] + u * boundary_voxel[6]);
	front.z = floor_pos.z;

	back = (1-v)*((1-u)*boundary_voxel[3] + u * boundary_voxel[4]) + v * ((1-u)*boundary_voxel[5] + u * boundary_voxel[7]);
	back.z = floor_pos.z+1;
	
	interpol_sampling_pos_f = (1-w) * front + w * back;


    /// transform from array space to texture space
    vec3 sampling_pos_texture_space_f = interpol_sampling_pos_f/vec3(volume_dimensions);

    // access the volume data
    return texture(volume_texture, sampling_pos_texture_space_f * obj_to_tex).r;
}

float
get_sample_data(vec3 in_sampling_pos){
#if 0
    return get_nearest_neighbour_sample(in_sampling_pos);
#else
	vec3 obj_to_tex = vec3(1.0)/max_bounds;
    return texture(volume_texture, in_sampling_pos * obj_to_tex).r;
	//return get_triliniear_sample(in_sampling_pos);
#endif

}

vec3
get_gradient(vec3 pos) {
    vec3 grad = vec3(1.0);
    float dens = 0.005;
    grad.x = get_sample_data(vec3(pos.x-dens,pos.y,pos.z)) - get_sample_data(vec3(pos.x+dens,pos.y,pos.z));
    grad.y = get_sample_data(vec3(pos.x,pos.y-dens,pos.z)) - get_sample_data(vec3(pos.x,pos.y+dens,pos.z));
    grad.z = get_sample_data(vec3(pos.x,pos.y,pos.z-dens)) - get_sample_data(vec3(pos.x,pos.y,pos.z+dens));
    return grad;
}

vec4
phong(vec3 pos, vec3 I) {
    vec3 ambientCol = vec3(0.3,0.3,0.3);
    vec3 diffuseCol = vec3(0.5,0.5,0.5);
    //vec3 specularCol = vec3(0.6,0.6,0.6);
    vec3 specularCol = vec3(1.0,1.0,1.0);
    
    material mat = material(vec4(0.0,3.0,0.0,1.0),vec4(0.0,5.0,0.0,1.0),vec4(0.0,7.0,0.0,1.0),100.0);
    
    vec3 E = normalize(camera_location-pos);
    vec3 N = normalize(get_gradient(pos));
    vec3 L = normalize(light_position-pos);
    
    vec3 H = normalize(E + L);
    
    vec3 ambient = ambientCol;
    ambient.r *= I.r;
    ambient.g *= I.g;
    ambient.b *= I.b;
    vec3 diffuse = diffuseCol * max(dot(L,N),0);
    diffuse.r *= I.r;
    diffuse.g *= I.g;
    diffuse.b *= I.b;
    vec3 specular = specularCol * pow(max(dot(H,N),0),mat.shine);
    specular.r *= I.r;
    specular.g *= I.g;
    specular.b *= I.b;
    
    vec4 out_Color = vec4(ambient + diffuse + specular, 1.0);
    
    return out_Color;
}

vec4
phong(vec3 pos) {
    vec3 ambientCol = vec3(0.3,0.3,0.3);
    vec3 diffuseCol = vec3(0.5,0.5,0.5);
    //vec3 specularCol = vec3(0.6,0.6,0.6);
    vec3 specularCol = vec3(1.0,1.0,1.0);
    
    material mat = material(vec4(0.0,3.0,0.0,1.0),vec4(0.0,5.0,0.0,1.0),vec4(0.0,7.0,0.0,1.0),100.0);
    
    vec3 E = normalize(camera_location-pos);
    vec3 N = normalize(get_gradient(pos));
    vec3 L = normalize(light_position-pos);
    
    vec3 H = normalize(E + L);
    
    vec3 ambient = ambientCol;
    vec3 diffuse = diffuseCol * max(dot(L,N),0);
    vec3 specular = specularCol * pow(max(dot(H,N),0),mat.shine);
    vec4 out_Color = vec4(ambient + diffuse + specular, 1.0);
    
    return out_Color;
}

#define AUFGABE 5  // 31 32 33 332 4 5
void main()
{
    /// One step trough the volume
    vec3 ray_increment      = normalize(ray_entry_position - camera_location) * sampling_distance;
    /// Position in Volume
    vec3 sampling_pos       = ray_entry_position + ray_increment; // test, increment just to be sure we are in the volume

    /// Init color of fragment
    vec4 dst = vec4(0.0, 0.0, 0.0, 0.0);

    /// check if we are inside volume
    bool inside_volume = inside_volume_bounds(sampling_pos);

#if AUFGABE == 31
    vec4 max_val = vec4(0.0, 0.0, 0.0, 0.0);
  
    // the traversal loop,
    // termination when the sampling position is outside volume boundarys
    // another termination condition for early ray termination is added
    while (inside_volume && max_val.a < 0.95) 
    {      
        // get sample
        float s = get_sample_data(sampling_pos);
                
        // apply the transfer functions to retrieve color and opacity
        vec4 color = texture(transfer_texture, vec2(s, s));
          
        // this is the example for maximum intensity projection
        max_val.r = max(color.r, max_val.r);
        max_val.g = max(color.g, max_val.g);
        max_val.b = max(color.b, max_val.b);
        max_val.a = max(color.a, max_val.a);
        
        // increment the ray sampling position
        sampling_pos  += ray_increment;

        // update the loop termination condition
        inside_volume  = inside_volume_bounds(sampling_pos);
    }

    dst = max_val;
#endif 
    
#if AUFGABE == 32
    
    // the traversal loop,
    // termination when the sampling position is outside volume boundarys
    // another termination condition for early ray termination is added
    float summe = 0;
    int count_samples = 0;
    while (inside_volume && dst.a < 0.95)
    {      
        // get sample
        float s = get_sample_data(sampling_pos);
        if(s >= 0.4) {
            summe += s;
            count_samples++;
        }
        // increment the ray sampling position
        sampling_pos  += ray_increment;

        // update the loop termination condition
        inside_volume  = inside_volume_bounds(sampling_pos);
    }
    summe /= count_samples;
    dst = texture(transfer_texture, vec2(summe, summe));
#endif
    
#if AUFGABE == 33
    // the traversal loop,
    // termination when the sampling position is outside volume boundarys
    // another termination condition for early ray termination is added
    vec3 I = vec3(0.0,0.0,0.0);
    float op = 1.0;
    while (inside_volume && dst.a < 0.95 && op > 0.1)
    {
        // get sample
        float s = get_sample_data(sampling_pos);
        // s ist die intensitaet des schaedels an einer stelle des rays
        vec3 color = (texture(transfer_texture, vec2(s, s))).rgb;
        float alpha = (texture(transfer_texture, vec2(s, s))).a;
        
        
        vec3 Itemp = vec3(0.0,0.0,0.0);
        Itemp.r = color.r*alpha;
        Itemp.g = color.g*alpha;
        Itemp.b = color.b*alpha;
        
        I.r += Itemp.r*op;
        I.g += Itemp.g*op;
        I.b += Itemp.b*op;
        
        
        op *= (1-alpha);
        // increment the ray sampling position
        sampling_pos += ray_increment;

        // update the loop termination condition
        inside_volume = inside_volume_bounds(sampling_pos);
    }
    dst = vec4(I,1.0);
#endif 

#if AUFGABE == 332
    // the traversal loop,
    // termination when the sampling position is outside volume boundarys
    // another termination condition for early ray termination is added
    vec3 I = vec3(0.0,0.0,0.0);
    float op = 1.0;
    while (inside_volume && dst.a < 0.95)
    {
        // increment the ray sampling position
        sampling_pos += ray_increment;

        // update the loop termination condition
        inside_volume = inside_volume_bounds(sampling_pos);
    }
    inside_volume = true;
    sampling_pos -= ray_increment;
    while (inside_volume && dst.a < 0.95) {
        // get sample
        float s = get_sample_data(sampling_pos);
        // s ist die intensitaet des schaedels an einer stelle des rays
        vec3 color = (texture(transfer_texture, vec2(s, s))).rgb;
        float alpha = (texture(transfer_texture, vec2(s, s))).a;
        
        
        vec3 Itemp = vec3(0.0,0.0,0.0);
        Itemp.r = color.r*alpha;
        Itemp.g = color.g*alpha;
        Itemp.b = color.b*alpha;
        
        I.r += Itemp.r*op;
        I.g += Itemp.g*op;
        I.b += Itemp.b*op;
        
        
        op *= (1-alpha);
        
        sampling_pos -= ray_increment;
        inside_volume = inside_volume_bounds(sampling_pos);
    }
    dst = vec4(I,1.0);
#endif 

#if AUFGABE == 4
    vec3 I = vec3(0.0,0.0,0.0);
    float op = 1.0;
    while (inside_volume && dst.a < 0.95 && op > 0.1)
    {
        // get sample
        float s = get_sample_data(sampling_pos);
        // s ist die intensitaet des schaedels an einer stelle des rays
        vec3 color = (texture(transfer_texture, vec2(s, s))).rgb;
        float alpha = (texture(transfer_texture, vec2(s, s))).a;
        
        
        vec3 Itemp = vec3(0.0,0.0,0.0);
        Itemp.r = color.r*alpha;
        Itemp.g = color.g*alpha;
        Itemp.b = color.b*alpha;
        
        I.r += Itemp.r*op;
        I.g += Itemp.g*op;
        I.b += Itemp.b*op;
        
        
        op *= (1-alpha);
        // increment the ray sampling position
        sampling_pos += ray_increment;

        // update the loop termination condition
        inside_volume = inside_volume_bounds(sampling_pos);
    }
    dst = phong(sampling_pos,I);
    
#endif 

#if AUFGABE == 5

    // the first hit traversal loop,
    // termination when the sampling position is outside volume boundarys
    // another termination condition for early ray termination is added
    bool ratio = true;
    if(get_sample_data(sampling_pos) < iso_value) {
        ratio = false;
    }
    
    float threshold = 0.01;
    while (inside_volume)
    {
        // get sample
        float s = get_sample_data(sampling_pos);
        // s ist die intensitaet des schaedels an einer stelle des rays
        
        if(abs(s - iso_value) <= threshold) {
            dst = phong(sampling_pos);
            break;
        }
        else if(s < iso_value == ratio) {
            ratio = !ratio;
            ray_increment = -ray_increment/2;
        }
        // increment the ray sampling position
        sampling_pos += ray_increment;

        // update the loop termination condition
        inside_volume = inside_volume_bounds(sampling_pos);
    }
    
#endif 

    // return the calculated color value
    FragColor = dst;
}

