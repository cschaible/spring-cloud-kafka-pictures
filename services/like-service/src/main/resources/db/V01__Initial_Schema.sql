CREATE TABLE `likes` (
  `like_id_image_identifier` varchar(255) NOT NULL,
  `like_id_user_identifier` varchar(255) NOT NULL,
  `insert_date` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `idp_id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;