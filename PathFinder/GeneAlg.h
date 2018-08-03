#ifndef GENEALG_H
#define GENEALG_H

#include <vector>
#include <string>

#include "defines.h"
#include "Map.h"
#include "utils.h"

using std::vector;
using std::string;

struct SGenome {
	vector<int> vecBits;
	double fitness;

	SGenome():fitness(0) {}
	SGenome(const int numBits):fitness(0)
	{
		for(int i=0; i<numBits; i++) 
			vecBits.push_back(RandInt(0,1));
	}

};

class GeneAlg {
	private:
		vector<SGenome> _vecGenomes;

		int 	_popSize;
		double 	_crossoverRate;
		double 	_mutationRate;
		int 	_chromoLength;
		int 	_geneLength;
		int 	_fittestGenome;
		double 	_bestFitnessScore;
		double 	_totalFitnessScore;
		int 	_generation;

		Map _benzMap;
		Map _benzBrain;

		bool _busy;

		void Mutate(vector<int> &vecBits);
		void Crossover(const vector<int> &mom,
				const vector<int> &dad,
				vector<int> &child1,
				vector<int> &chil2);

		SGenome& RouletteWheel();
		void UpdateFitnessScore();
		vector<int> Decode(const vector<int> &bits);
		int BinToInt(const vector<int> &v);
		void CreateStartPop();

	public:
		GeneAlg(double cross,
			double mut,
			int pop,
			int bits,
			int gene):_crossoverRate(cross),
				  _mutationRate(mut),
				  _popSize(pop),
				  _chromoLength(bits),
				  _totalFitnessScore(0.0),
				  _generation(0),
				  _geneLength(gene),
				  _busy(false)
		{ CreateStartPop(); }

		void Run();
		void Render();
		void Epoch();

		int Generation() { return _generation; }
		int GetFittest() { return _fittestGenome; }
		string Chromosome();

		bool Started() { return _busy; }
		void Stop() { _busy = false; }
};

#endif
