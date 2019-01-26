#version 130

uniform vec3 uni_camPosition;

uniform vec3 uni_objectColor;

in vec2 var_UV;
in vec3 var_Normal;
in vec3 var_Position;

out vec4 finalColor;

in vec3 var_color;

void main()
{
	
    vec3 transformedNormal = normalize(var_Normal);
	vec3 viewVector = normalize(var_Position - uni_camPosition);
	
	finalColor = vec4(var_color, 1.0);
}
