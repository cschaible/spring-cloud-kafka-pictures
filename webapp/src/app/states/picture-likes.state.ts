import { Action, Selector, State, StateContext } from "@ngxs/store";
import { LikesService } from "../services/likes.service";
import { LikesResource } from "../resources/likes/LikesResource";

export class FindLikesAction {
  static readonly type = 'FindLikes';

  constructor(public identifier: string) {
  }
}

export class LikePictureAction {
  static readonly type = 'LikePictureAction';

  constructor(public identifier: string) {
  }
}

export class DislikePictureAction {
  static readonly type = 'DislikePictureAction';

  constructor(public identifier: string) {
  }
}

export class LikesState {
  likes: LikesResource[] = [];
  initialized: boolean = false;
}

@State<LikesState>({
  name: 'pictureLikes'
})
export class PictureLikesState {

  constructor(private likesService: LikesService) { }

  @Action(FindLikesAction)
  findPicture(ctx: StateContext<LikesState>, action: FindLikesAction) {
    this.likesService.getLikesForImage(action.identifier).subscribe(response => {
      let model = ctx.getState();
      if (!model.initialized) {
        model = new LikesState();
        model.initialized = true;
        ctx.setState(model);
      }
      ctx.patchState({
        likes: [...model.likes.filter(like => like.imageIdentifier != response.imageIdentifier), response]
      });
    });
  }

  @Action(LikePictureAction)
  likesPicture(ctx: StateContext<LikesState>, action: LikePictureAction) {
    this.likesService.likeImages(action.identifier).subscribe(response => {
      let model = ctx.getState();
      ctx.patchState({
        likes: [...model.likes.filter(like => like.imageIdentifier != response.imageIdentifier), response]
      });
    });
  }

  @Action(DislikePictureAction)
  dislikesPicture(ctx: StateContext<LikesState>, action: DislikePictureAction) {
    this.likesService.dislikeImages(action.identifier).subscribe(response => {
      let model = ctx.getState();
      ctx.patchState({
        likes: [...model.likes.filter(like => like.imageIdentifier != response.imageIdentifier), response]
      });
    });
  }

  @Selector()
  static likesForImage(state: LikesState, imageIdentifier: string): number {
    let likes = state.likes.find(item => item.imageIdentifier === imageIdentifier);
    return likes ? likes.likes : 0;
  }

}
