#ifndef MAP_H
#define MAP_H

#include <cstdlib>
#include <vector>
#include <iostream>
#include "defines.h"

using std::vector;
using std::cout;

class Map {
	private:
		static const int map[MAP_HEIGHT][MAP_WIDTH];

		static const int _mapWidth, _mapHeight;
		static const int _startX, _startY;
		static const int _endX, _endY;

	public:
		int memory[MAP_HEIGHT][MAP_WIDTH]; 

		Map();
		double TestRoute(const vector<int> &vecPath, Map &memory);
		void Render();
		void MemoryRender();
		void ResetMemory();
};

#endif
