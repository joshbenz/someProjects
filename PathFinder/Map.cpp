#include <ncurses.h>
#include "Map.h"
#include <iostream>
using namespace std;
//the map
const int Map::map[MAP_HEIGHT][MAP_WIDTH] = 
{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1,
 8, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1,
 1, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1,
 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1,
 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1,
 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1,
 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 5,
 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1,
 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

const int Map::_mapHeight = MAP_HEIGHT;
const int Map::_mapWidth = MAP_WIDTH;

const int Map::_startX = 0;
const int Map::_startY = 2;

const int Map::_endX = 14;
const int Map::_endY = 7;

Map::Map()
{
	ResetMemory();
}

void Map::Render() 
{
	for(int y=0; y<_mapHeight; y++) {
		for(int x=0; x<_mapWidth; x++) {
			switch(map[y][x]) {
				case 0:
					mvaddch(y+5,x+5,' ');
					break;
				case 1:
					mvaddch(y+5,x+5,'Z');
					break;
				case 8:
					mvaddch(y+5,x+5,'Q');
					break;
				case 5:
					mvaddch(y+5,x+5,'E');
					break;
				defualt:
					break;
			}
		}
	}
}

void Map::MemoryRender()
{
	for(int y=0; y<_mapHeight; y++) {
		for(int x=0; x<_mapWidth; x++) {
			if(memory[y][x] == 1) {
				mvaddch(y+5,x+5,'+');
			}
		}
	}
}

double Map::TestRoute(const vector<int> &vecPath, Map &Benz)
{
	int posX = _startX;
	int posY = _startY;

	for(int i=0; i<vecPath.size(); i++) {
		switch(vecPath[i]) {
			case 0: //North
			//within bounds and can move
				if(((posY-1) < 0) || (map[posY-1][posX] == 1)) {
					break;
				} else {
					posY -= 1;
				}
				break;

			case 1: //South
				if(((posY+1) >= _mapHeight) || (map[posY+1][posX] == 1)) {
					break;
				} else {
					posY += 1;
				}
				break;

			case 2: //East
				if(((posX+1) >= _mapWidth) || (map[posY][posX+1] == 1)) {
					break;
				} else {
					posX += 1;
				}
				break;

			case 3: //West
				if(((posX-1) < 0) || (map[posY][posX-1] == 1)) {
					break;
				} else {
					posX -= 1;
				}
				break;

			default:
				break;
		}
		Benz.memory[posY][posX] = 1;
	}

	int diffX = abs(posX - _endX);
	int diffY = abs(posY - _endY);

	return 1/(double)(diffX+diffY+1);
}

void Map::ResetMemory()
{
	for(int y=0; y<_mapHeight; y++) {
		for(int x=0; x<_mapWidth; x++) {
			memory[y][x] = 0;
		}
	}
}

