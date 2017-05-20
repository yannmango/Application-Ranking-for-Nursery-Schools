ID3 <- function(dataset, target_attr,

					attributes=setdiff(names(dataset), target_attr)) {

	# If there are no attributes left to classify with,

	# return the most common class left in the dataset

	# as a best approximation.

	if (length(attributes) <= 0) {

		# DEBUG: print("attributes ran out")

		return(node(most.frq(dataset[, target_attr])))

	}



	# If there is only one classification left, return a

	# node with that classification as the answer

	if (length(unique(dataset[, target_attr])) == 1) {

		# DEBUG: print('one class left')

		return(node(unique(dataset[, target_attr])[1]))

	}



	# Select the best attribute based on the minimum entropy

	best_attr <- attributes[which.min(sapply(attributes, entropy))]

	# Create the set of remaining attributes

	rem_attrs <- setdiff(attributes, best_attr)

	# Split the dataset into groups based on levels of the best_attr

	split_dataset <- split(dataset, dataset[, best_attr])

	# Recursively branch to create the tree.

	branches <- lapply(seq_along(split_dataset), function(i) {

			# The name of the branch

			name <- names(split_dataset)[i]

			# Get branch data

			branch <- split_dataset[[i]]

			

			# If there is no data, return the most frequent class in

			# the parent, otherwise start over with new branch data.

			if (nrow(branch) == 0) node(most.frq(dataset[, target_attr]))

			else ID3(branch[, union(target_attr, rem_attrs), drop=FALSE],

												target_attr,

												rem_attrs)

			})

	names(branches) <- names(split_dataset)
	id3_tree <- tree(root=best_attr, branches=branches)

	id3_tree

}
