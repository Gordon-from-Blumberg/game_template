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

void main()
{
  vec2 st = gl_FragCoord.xy / u_resolution;
  v_color.r = st.x;
  v_color.b = st.y;
  //v_color.g = sin(u_time*20.) * .5 + .5;
  float d = distance(st, u_mouse);
  d = d*d;
  gl_FragColor = v_color * texture2D(u_texture, v_texCoords) - 0.01/ d;
}