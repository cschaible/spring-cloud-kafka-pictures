import {Action, Selector, State, StateContext} from "@ngxs/store";
import {PictureSearchService} from "../services/picture-search.service";
import {PictureResource} from "../resources/picture/PictureResource";
import {UpdatePictureTitleAction} from "./update-picture-title-action";

export class FindPictureAction {
  static readonly type = 'FindPicture';

  constructor(public identifier: string) {
  }
}

@State<PictureResource>({
  name: 'pictureFullsize',
})
export class PictureFullsizeState {

  constructor(private pictureSearchService: PictureSearchService) {
  }


  @Action(FindPictureAction)
  findPicture(ctx: StateContext<PictureResource>, action: FindPictureAction) {

    //Search and update the state with returned data
    this.pictureSearchService.findOne(action.identifier).subscribe(picture => ctx.setState(picture));
  }

  @Action(UpdatePictureTitleAction)
  updatePictureTitle(ctx: StateContext<PictureResource>, action: UpdatePictureTitleAction) {
    this.pictureSearchService.updateTitle(action.identifier, action.title).subscribe(picture => ctx.setState(picture));
  }

  @Selector()
  static pictureMediumUrl(state: PictureResource) {
    return state._links.imageMedium.href;
  }

  @Selector()
  static profilePictureUrl(state: PictureResource) {
    return state._embedded.user._links.profilePictureUrl.href
  }

  @Selector()
  static user(state: PictureResource) {
    return state._embedded.user;
  }
}
