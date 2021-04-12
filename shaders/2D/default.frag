#version 330 core

struct Material {
    sampler2D diffuse;
    sampler2D specular;
    sampler2D normal;
    float shininess;
};

layout (location = 0) out vec4 color;

uniform Material material;

in vec2 UV;
in vec4 tint;

void main () {
    color = texture((material.diffuse), UV).rgba * tint;
}