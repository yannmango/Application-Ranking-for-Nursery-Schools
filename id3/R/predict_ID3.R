predict_ID3 <- function(test_obs, id3_tree) {

	traverse <- function(obs, work_tree) {

		if (class(work_tree) == 'node') work_tree$root

		else {

			var <- work_tree$root

			new_tree <- work_tree$branches[[as.character(obs[var])]]

			traverse(obs, new_tree)

		}

	}

	apply(test_obs, 1, traverse, work_tree=id3_tree)

}
