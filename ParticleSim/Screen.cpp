#include "Screen.h"

namespace Benz {
Screen::Screen(): 
        _window(NULL),
        _renderer(NULL),
        _texture(NULL),
        _buffer(NULL),
        _blurBuffer(NULL) {}

bool Screen::init() {
    if(SDL_Init(SDL_INIT_VIDEO) < 0) {
        return false;
    }

    _window = SDL_CreateWindow("Particles", 
                                SDL_WINDOWPOS_UNDEFINED,
                                SDL_WINDOWPOS_UNDEFINED,
                                SCREEN_WIDTH,
                                SCREEN_HEIGHT,
                                SDL_WINDOW_SHOWN);
    if(_window == NULL) {
        SDL_Quit();
        return false;
    }

    _renderer = SDL_CreateRenderer(_window, -1, SDL_RENDERER_PRESENTVSYNC);
    _texture = SDL_CreateTexture(_renderer,
                                SDL_PIXELFORMAT_RGBA8888,
                                SDL_TEXTUREACCESS_STATIC,
                                SCREEN_WIDTH,
                                SCREEN_HEIGHT);
    if(_renderer == NULL) {
        SDL_DestroyWindow(_window);
        SDL_Quit();
        return false;
    }

    if(_texture == NULL) {
        SDL_DestroyRenderer(_renderer);
        SDL_DestroyWindow(_window);
        SDL_Quit();
        return false;
    }

    _buffer = new Uint32[SCREEN_WIDTH * SCREEN_HEIGHT];
    _blurBuffer = new Uint32[SCREEN_WIDTH * SCREEN_HEIGHT];

    memset(_buffer, 0, SCREEN_WIDTH * SCREEN_HEIGHT * sizeof(Uint32));

    /*for(int i=0; i<SCREEN_WIDTH * SCREEN_HEIGHT; i++) {
       // _buffer[i] = 0xFFFFFFFF;
    }*/

    return true;
}

bool Screen::processEvents() {
    SDL_Event event;

    while(SDL_PollEvent(&event)) {
        if(event.type == SDL_QUIT) {
            return false;
        }
    }
    return true;
}

void Screen::update() {
    SDL_UpdateTexture(_texture, NULL, _blurBuffer, SCREEN_WIDTH * sizeof(Uint32));
    SDL_RenderClear(_renderer);
    SDL_RenderCopy(_renderer, _texture, NULL, NULL);
    SDL_RenderPresent(_renderer);
}

void Screen::setPixel(int x, int y, Uint8 red, Uint8 green, Uint8 blue) {
    if(x < 0 || x >= SCREEN_WIDTH || y < 0 || y >= SCREEN_HEIGHT) {
        return;
    }
    Uint32 color = 0;

    color += red;
    color <<= 8;

    color += green;
    color <<= 8;

    color += blue;
    color <<= 8;

    color += 0xFF;
    _blurBuffer[(y * SCREEN_WIDTH) + x] = color;
}

void Screen::clear() {
    memset(_buffer, 0, SCREEN_WIDTH * SCREEN_HEIGHT * sizeof(Uint32));
    memset(_blurBuffer, 0, SCREEN_WIDTH * SCREEN_HEIGHT * sizeof(Uint32));
}

void Screen::boxBlur() {
    Uint32 *tmp = _buffer;
    _buffer = _blurBuffer;
    _blurBuffer = tmp;

    for(int y=0; y<SCREEN_HEIGHT; y++) {
        for(int x=0; x<SCREEN_WIDTH; x++) {

            int redTotal = 0, greenTotal = 0, blueTotal = 0;
            for(int row=-1; row<=1; row++) {
                for(int col=-1; col<=1; col++) {
                    int currX = x + col;
                    int currY = y + row;

                    if(currX >= 0 && currX < SCREEN_WIDTH && currY >= 0 && currY < SCREEN_HEIGHT) {
                        Uint32 color = _blurBuffer[currY*SCREEN_WIDTH + currX];

                        Uint8 red = color >> 24;
                        Uint8 blue = color >> 16;
                        Uint8 green = color >> 8;

                        redTotal += red;
                        blueTotal += blue;
                        greenTotal += green;
                    }
                }
            }

            Uint8 red = redTotal / 9;
            Uint8 blue = blueTotal / 9;
            Uint8 green = greenTotal / 9;

            setPixel(x, y, red, green, blue);
        }
    }
}

void Screen::close() {
    delete[] _buffer;
    delete[] _blurBuffer;
    SDL_DestroyRenderer(_renderer);
    SDL_DestroyTexture(_texture);
    SDL_DestroyWindow(_window);
    SDL_Quit();
}

} //end namespace