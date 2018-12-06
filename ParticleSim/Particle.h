#ifndef PARTICLE_H_
#define PARTICLE_H_

namespace Benz {

struct Particle {

    double x;
    double y;

    double speed;
    double direction;

public:
    Particle();
    virtual ~Particle();
    void update(int interval);
    void init();

}; //end class
}//end namespace

#endif