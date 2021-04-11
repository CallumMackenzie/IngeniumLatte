#version 330 core

layout (location = 0) in vec2 vertexPos;
layout (location = 1) in vec2 vertexUV;
layout (location = 2) in vec4 vertexRGB;

uniform mat2 model;
uniform vec4 modelTint;
uniform vec2 translation;
uniform float aspect;
uniform mat2 camera;

out vec2 UV;
out vec4 tint;

void main () {
    UV = vertexUV;
    tint = vertexRGB + modelTint;
    // vec2 transformed = (model * vertexPos) + translation;
    vec2 transformed = vertexPos + translation;
    gl_Position = vec4(transformed.x, transformed.y * aspect, 0.0, 1.0);
}