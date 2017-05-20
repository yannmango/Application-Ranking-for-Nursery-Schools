entropy <- function(S) {

	if (!is.factor(S)) S <- as.factor(S)



	p <- prop.table(table(S))



	-sum(sapply(levels(S),

		function(name) p[name] * log2(p[name]))

	)
