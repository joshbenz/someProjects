#ifndef SWARM_H_
#define SWARM_H_

#include "Particle.h"

namespace Benz {

class Swarm {
public:
    const static int NPARTICLES = 5000;

private:
    Particle *_pParticles;
    int _lastTime;

public:
    Swarm();
    virtual ~Swarm();
    const Particle *const getParticles() { return _pParticles; };
    void update(int elapsed);
};//end class

}//end namespace

#endif