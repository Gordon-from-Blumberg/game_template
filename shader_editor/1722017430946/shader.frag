#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

uniform sampler2D u_texture;
uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

vec2 random2(vec2 st) {
    st = vec2( dot(st, vec2(127.1, 311.7)),
              dot(st, vec2(269.5, 183.3)) );
    return -1.0 + 2.0*fract(sin(st)*43758.5453123);
}

float noise1(vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    vec2 u = f*f*(3.0-2.0*f);

    return mix( mix( dot( random2(i + vec2(0.0,0.0) ), f - vec2(0.0,0.0) ),
                     dot( random2(i + vec2(1.0,0.0) ), f - vec2(1.0,0.0) ), u.x),
                mix( dot( random2(i + vec2(0.0,1.0) ), f - vec2(0.0,1.0) ),
                     dot( random2(i + vec2(1.0,1.0) ), f - vec2(1.0,1.0) ), u.x), u.y);
}

void main() {
  vec2 st = gl_FragCoord.xy / u_resolution;
  vec3 clr1 = vec3(.8, .7, .2);
  vec3 clr2 = vec3(.02, .70, .35);
//  gl_FragColor = vec4(vec3(noise1(st*20.)), 1.);
  gl_FragColor = vec4(clr2, 1.);
}