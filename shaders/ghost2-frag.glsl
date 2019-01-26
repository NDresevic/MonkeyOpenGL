#version 130

uniform vec3 uni_camPosition;

uniform vec3 uni_objectColor;

in vec2 var_UV;
in vec3 var_Normal;
in vec3 var_Position;

out vec4 finalColor;

void main()
{
	
    vec3 transformedNormal = normalize(var_Normal);
    
	vec3 viewVector = normalize(var_Position - uni_camPosition);
	float silhouette = dot(viewVector, transformedNormal);
	silhouette = pow(1.0 - abs(silhouette), 1.0);
	
	finalColor = vec4(uni_objectColor * silhouette, 1.0);
}
