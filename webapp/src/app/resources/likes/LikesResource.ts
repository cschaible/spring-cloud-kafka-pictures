import { ResourceLink } from '../ResourceLink';

export class LikesResource {
  public imageIdentifier: string;
  public likes: number;
  public hasLiked: boolean;

  public _links?: LikeLinks;
}

export class LikeLinks {
  public self: ResourceLink;
  public like?: ResourceLink;
  public dislike?: ResourceLink;
}

