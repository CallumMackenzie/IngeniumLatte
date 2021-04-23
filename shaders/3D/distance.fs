#version $version(300 es)$

#ifdef GL_ES
precision $precision(mediump)$ float;
#endif

layout (location = 0) out vec4 color;

void main () {
    color = vec4(1.0, 1.0, 1.0, 1.0);
}