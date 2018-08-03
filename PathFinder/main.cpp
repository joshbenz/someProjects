//compiled with g++ -std=c++11 -lncurses 
#include <cstdlib>
#include <ncurses.h>
#include "GeneAlg.h"
#include "defines.h"

using namespace std;

GeneAlg* genetic;

int main()
{

	genetic = new GeneAlg(CROSSOVER_RATE,
				MUTATION_RATE,
				POP_SIZE,
				CHROMO_LENGTH,
				GENE_LENGTH);
	genetic->Run();
	initscr();
	cbreak();
	noecho();
	while(genetic->Started()) {
		clear();
		genetic->Epoch();
		printw("Generation %i\n",genetic->Generation());
		printw("Fittest Genome: %s", genetic->Chromosome().c_str());
		genetic->Render();
		refresh();
	}
	getchar();
	endwin();

	return 0;
}
