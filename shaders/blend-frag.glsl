#version 130

uniform sampler2D uni_texture;

uniform vec3 uni_objectColor;
uniform vec3 uni_camPosition;

in vec2 var_UV;
in vec3 var_Normal;
in vec3 var_Position;
in vec3 var_Color;

out vec4 finalColor;

void main()
{
    vec3 transformedNormal = normalize(var_Normal);
    vec3 viewVector = normalize(var_Position - uni_camPosition);
	
    finalColor = texture(uni_texture, var_UV) * vec4(uni_objectColor, 0.1f);
}
