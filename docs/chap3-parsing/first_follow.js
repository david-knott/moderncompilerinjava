let grammar = [
	{l: 'Z', r: ['d']},
	{l: 'Z', r: ['X', 'Y', 'Z']},
	{l: 'Y', r: ['']},
	{l: 'Y', r: ['c']},
	{l: 'X', r: ['Y']},
	{l: 'X', r: ['a']}
];

var nullableSet = {};

/**
 * 
 * @param {The grammar to generate a nullable set for} grammar 
 * @param {The nullable set} nullableSet 
 */
let nullable2 = function(grammar, nullableSet){
	let hasChanged = false;
	do {
		for(let i = 0; i < grammar.length; i++){
			let lsymbol = grammar[i].l;
			//initialize the nullable set for our left symbol
			nullableSet[lsymbol] = nullableSet[lsymbol] | {};
			let rsymbols = grammar[i].r;
			var nulls = [];
			//check all symbols on right side to see if they are empty
			//also check our nullableSet for items that are in the nullableset
			var allNull = true;
			for(let j = 0; j < rsymbols.length; j++){
				let s = rsymbols[j];
				//is the current symbol empty or is it a symbol we already
				//know that evaluates to empty
				if(s.length == 0 || !!nullableSet[s])	{
					nulls[j] = true;
					setChanged = true;
				} else {
					nulls[j] = false;
					allNull = false;
				}
			}
			//check all items are empty, then lsymbol is empty
			if(allNull) {
				nullableSet[lsymbol] = true;
			}
		}
	}
	while(hasChanged);
	return nullableSet;
}

console.log(nullable2(grammar, nullableSet));