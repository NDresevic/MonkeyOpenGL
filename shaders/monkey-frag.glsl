#version 130

uniform samplerCube uni_environment;

uniform vec3 uni_camPosition;

in vec2 var_UV;
in vec3 var_Normal;
in vec3 var_Position;

out vec4 finalColor;

void main()
{
    vec3 transformedNormal = normalize(var_Normal);
    vec3 viewVector = normalize(var_Position - uni_camPosition);
    vec3 reflected = reflect(viewVector, transformedNormal);
    reflected.x = reflected.x * -5.0;
    reflected.z = reflected.z * 70.0;
    
    finalColor = texture(uni_environment, reflected);
}
