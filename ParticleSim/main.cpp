#include <iostream>
#include <SDL2/SDL.h>
#include <cmath>
#include <cstdlib>
#include <ctime>
#include "Screen.h"
#include "Swarm.h"

using namespace std;
using namespace Benz;

int main() {
    srand(time(NULL));
    Screen screen;

    if(!screen.init()) {
        cout << "Error init SDL" << endl;
    }

    Swarm swarm;

    while(true) {
        int elapsed = SDL_GetTicks();

        //screen.clear();
        swarm.update(elapsed);
        
        unsigned char green = (unsigned char)((1 + sin(elapsed * 0.0001)) * 128);
        unsigned char red = (unsigned char)((1 + sin(elapsed * 0.0002)) * 128);
        unsigned char blue = (unsigned char)((1 + sin(elapsed * 0.0003)) * 128);

        //update particles
        const Particle *const pParticles = swarm.getParticles();
        for(int i=0; i<Swarm::NPARTICLES; i++) {
            Particle particle = pParticles[i];
            int x = (particle.x +1) * Screen::SCREEN_WIDTH / 2;
            int y = particle.y  * Screen::SCREEN_WIDTH / 2 + Screen::SCREEN_HEIGHT/2;

            screen.setPixel(x, y, red, green, blue);
        }

        screen.boxBlur();
        //draw particles
        screen.update();
        //check msgs

        if(!screen.processEvents()) {
            break;
        }
    }

    screen.close();

    return 0;
}
