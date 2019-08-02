CREATE TABLE `likes` (
  `image` varchar(255) DEFAULT NULL,
  `likes` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `user` (
  `id` varchar(255) DEFAULT NULL,
  `idp_id` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;