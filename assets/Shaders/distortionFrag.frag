#import "Common/ShaderLib/Parallax.glsllib"
#ifdef COLOR
    uniform vec4 m_Color;
#endif

#ifdef COLORMAP
    uniform sampler2D m_ColorMap;
#endif

#ifdef BURNMAP
    uniform sampler2D m_BurnMap;
    #ifdef PARAM
        uniform vec2 m_Param;
    #endif
    
#endif

varying vec4 texCoord;

void main(){
    vec4 color = vec4(1.0);
    
    #ifdef COLOR
        color = m_Color;
    #endif
    
    #ifdef COLORMAP
        color *= texture2D(m_ColorMap, texCoord.xy); 
        //        color *= texture2D(m_ColorMap, texCoord); //

    #endif
    
    

    
    
    gl_FragColor = color;
    }