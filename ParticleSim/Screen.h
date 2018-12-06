#ifndef SCREEN_H_
#define SCREEN_H_

#include<SDL2/SDL.h>

namespace Benz {

class Screen {
public:
    const static int SCREEN_WIDTH = 800;
    const static int   SCREEN_HEIGHT = 600;

private:
    SDL_Window *_window;
    SDL_Renderer *_renderer;
    SDL_Texture *_texture;
    Uint32 *_buffer;
    Uint32 *_blurBuffer;
    

public:
    Screen();
    bool init();
    bool processEvents();
    void close();
    void update();
    void setPixel(int x, int y, Uint8 red, Uint8 green, Uint8 blue);
    void clear();
    void boxBlur();

}; //end class
} //nd namespace
#endif