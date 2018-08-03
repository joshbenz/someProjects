#include <algorithm>
#include <string>
#include <sstream>

#include <ncurses.h>
#include "GeneAlg.h"

/*
 * adds up the fitness scores as it goes until it gets above the
 * total fitness. note: this does not garuntee the fittest genome
 */

SGenome& GeneAlg::RouletteWheel()
{
	double slice = RandFloat() * _totalFitnessScore; 
	double total = 0.0;
	int selectedG = 0;

	for(int i=0; i<_popSize; i++) { //for every genome
		total += _vecGenomes[i].fitness; //add the fitness levels to the previous added

		if(total >= slice) { //if the total os greater than the slice
			selectedG = i; //save that Genome cuz its the new highest fitness
			break;
		}
	}
	return _vecGenomes[selectedG]; 
}

/*
 * flibs bits in the genomes chromosome
 */

void GeneAlg::Mutate(vector<int> &vecBits)
{
	for(int curr=0; curr<vecBits.size(); curr++) {
		if(RandFloat() < _mutationRate) {
			vecBits[curr] = !vecBits[curr];
		}
	}
}

void GeneAlg::Crossover(const vector<int> &mom,
			const vector<int> &dad,
			vector<int> &child1,
			vector<int> &child2)
{
	if((RandFloat() > _crossoverRate) || mom == dad) {
		child1 = mom; //if they are qual then we have
		child2 = dad; //nothing to do
		return;
	}

	int cp = RandInt(0, _chromoLength-1); //random copy point

	for(int i=0; i<cp; i++) {
		child1.push_back(mom[i]);
		child2.push_back(dad[i]);
	}

	for(int i=cp; i<mom.size(); i++) {
		child1.push_back(dad[i]);
		child2.push_back(mom[i]);
	}
}

void GeneAlg::Run()
{
	CreateStartPop();
	_busy = true;
}

void GeneAlg::CreateStartPop()
{
	//clear existing pop
	_vecGenomes.clear();

	for(int i=0; i<_popSize; i++) {
		_vecGenomes.push_back(SGenome(_chromoLength));
	}

	//reset
	_generation		= 0;
	_fittestGenome		= 0;
	_bestFitnessScore	= 0.0;
	_totalFitnessScore	= 0;
}

void GeneAlg::Epoch()
{
	UpdateFitnessScore();
	int noobs = 0;

	vector<SGenome> vecBabies;

	while(noobs < _popSize) {
		SGenome mom = RouletteWheel();
		SGenome dad = RouletteWheel();

		SGenome child1, child2;
		Crossover(mom.vecBits, dad.vecBits, child1.vecBits, child2.vecBits);

		Mutate(child1.vecBits);
		Mutate(child2.vecBits);

		vecBabies.push_back(child1);
		vecBabies.push_back(child2);
		
		noobs += 2;
	}
	_vecGenomes = vecBabies;
	_generation++;
}

void GeneAlg::UpdateFitnessScore()
{
	_fittestGenome 		= 0;
	_bestFitnessScore 	= 0;
	_totalFitnessScore	= 0;

	Map tmpMemory;

	for(int i=0; i<_popSize; i++) {
		vector<int> vecDirs = Decode(_vecGenomes[i].vecBits); //decode directions
		_vecGenomes[i].fitness = _benzMap.TestRoute(vecDirs, tmpMemory); //get fitness from test route
		_totalFitnessScore += _vecGenomes[i].fitness; // addfitness scores up..use for roullette wheel

		if(_vecGenomes[i].fitness > _bestFitnessScore) { //update highest fitness so far
			_bestFitnessScore = _vecGenomes[i].fitness;
			_fittestGenome = i;
			_benzBrain = tmpMemory;			//copy memory of fittest gnome

			if(_vecGenomes[i].fitness == 1) {
				_busy = false;
			}
		}
		tmpMemory.ResetMemory();
	}
}

vector<int> GeneAlg::Decode(const vector<int> &bits)
{
	vector<int> dirs;

	for(int gene=0; gene < bits.size(); gene += _geneLength) {
		vector<int> theGene;

		for(int bit=0; bit<_geneLength; bit++) {
			theGene.push_back(bits[gene+bit]);
		}

		dirs.push_back(BinToInt(theGene));
	}
	return dirs;
}

int GeneAlg::BinToInt(const vector<int> &vec)
{
	int val = 0;
	int multi = 1;

	for(int bit=vec.size(); bit>0; bit--) {
		val += vec[bit-1] * multi;
		multi *= 2;
	}
	return val;
}

void GeneAlg::Render()
{
	_benzMap.Render(); //map
	_benzBrain.MemoryRender(); //best route
}

string GeneAlg::Chromosome()
{
	std::vector<int>::const_iterator it;
	std::stringstream s;
	SGenome g = _vecGenomes[_fittestGenome];

	for(it=g.vecBits.begin(); it != g.vecBits.end(); it++) {
		if(it != g.vecBits.begin())
			s << "";
		s << *it;
	}

	return s.str();
	

}
