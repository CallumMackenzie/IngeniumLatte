#version $version(300 es)$

#ifdef GL_ES
precision $precision(mediump)$ float;
#endif

layout (location = 0) out vec4 color;

struct Material {
    sampler2D diffuse;
};

uniform Material material;
in vec2 UV;

const float offset = 1.0 / 300.0;  

void main() {
    // vec2 offsets[9] = vec2[](
    //     vec2(-offset,  offset), // top-left
    //     vec2( 0.0f,    offset), // top-center
    //     vec2( offset,  offset), // top-right
    //     vec2(-offset,  0.0f),   // center-left
    //     vec2( 0.0f,    0.0f),   // center-center
    //     vec2( offset,  0.0f),   // center-right
    //     vec2(-offset, -offset), // bottom-left
    //     vec2( 0.0f,   -offset), // bottom-center
    //     vec2( offset, -offset)  // bottom-right    
    // );

    // float sharpenKernel[9] = float[](
    //     -1.0, -1.0, -1.0,
    //     -1.0,  9.0, -1.0,
    //     -1.0, -1.0, -1.0
    // );

    // float blurKernel[9] = float[](
    //     1.0 / 16.0, 2.0 / 16.0, 1.0 / 16.0,
    //     2.0 / 16.0, 4.0 / 16.0, 2.0 / 16.0,
    //     1.0 / 16.0, 2.0 / 16.0, 1.0 / 16.0  
    // );

    // vec3 sampleTex[9];
    // for(int i = 0; i < 9; i++)
    // {
    //     sampleTex[i] = vec3(texture(material.diffuse, UV.st + offsets[i]));
    // }
    // vec3 col = vec3(0.0);
    // for(int i = 0; i < 9; i++)
    //     col += sampleTex[i] * sharpenKernel[i];
    // color = vec4(col, 1.0);
    color = texture(material.diffuse, UV.st).rgba;
}