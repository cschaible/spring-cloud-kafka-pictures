import {Action, State, StateContext, Store} from "@ngxs/store";
import {PicturePageResource} from "../resources/picture/PicturePageResource";
import {PictureSearchService} from "../services/picture-search.service";
import {UpdatePictureTitleAction} from "./update-picture-title-action";
import {FindLikesAction} from "./picture-likes.state";

export class SearchImageAction {
  static readonly type = 'SearchImage';

  constructor(public term: string) {
  }
}

@State<PicturePageResource>({
  name: 'pictures',
})
export class PictureSearchState {

  constructor(private pictureSearchService: PictureSearchService, private store: Store) {
  }


  @Action(SearchImageAction)
  searchImage(ctx: StateContext<PicturePageResource>, action: SearchImageAction) {

    //Search and update the state with returned data
    this.pictureSearchService.search(action.term).subscribe(
      page => {
        ctx.setState(page)
        page.content.forEach(picture => this.store.dispatch(new FindLikesAction(picture.identifier)))
      }
    );
  }

  @Action(UpdatePictureTitleAction)
  updateTitle(ctx: StateContext<PicturePageResource>, action: UpdatePictureTitleAction) {
    let page = ctx.getState();
    let picture = page.content.find(p => p.identifier == action.identifier);
    if (picture) {
      picture.title = action.title;
    }
  }
}
