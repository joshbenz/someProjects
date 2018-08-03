#ifndef UTILS_H
#define UTILS_H

#include <random>
#include <cstdlib>

inline int RandInt(int x, int y) 
{
	std::random_device rd;
	std::mt19937 mt(rd());
	std::uniform_int_distribution<int> dist(x,y);
	return dist(mt);
}

inline double RandFloat()
{
	std::random_device rd;
	std::mt19937 mt(rd());
	std::uniform_real_distribution<float> dist(0.0,1.0);
	return dist(mt);
}

inline bool RandBool()
{
	if(RandInt(0,1)) return true;
	else return false;
}

inline double RandomClamped()
{
	return RandFloat() - RandFloat();
}
#endif
