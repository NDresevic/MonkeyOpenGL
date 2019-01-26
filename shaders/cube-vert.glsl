#version 130

in vec3 in_Position;
in vec2 in_UV;
in vec3 in_Normal;

uniform mat4 uni_VP;
uniform mat4 uni_M;

out vec3 var_Normal;
out vec2 var_UV;
out vec3 var_Position;

out vec3 var_color;

void main()
{
    mat4 matMVP = uni_VP * uni_M;
    var_Position = (uni_M * vec4(in_Position, 1.0)).xyz;
    gl_Position = matMVP * vec4(in_Position * 0.5, 1.0);

    var_UV = in_UV;
    
    var_color = vec3(in_Position.x + 0.5, in_Position.y + 0.5, in_Position.z + 0.5);

    vec3 transformedNormal = (uni_M * vec4(in_Normal, 0.0)).xyz;
    var_Normal = transformedNormal;
}
