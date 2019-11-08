import { ChangeDetectorRef, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { PictureResource } from '../../resources/picture/PictureResource';
import { Store } from '@ngxs/store';
import { AddCommentAction, CommentsState, FindCommentAction, ImageComment } from '../../states/comment.state';
import { Observable, of } from 'rxjs';
import { PictureService } from '../../services/picture.service';
import { LikePictureAction, DislikePictureAction } from "../../states/picture-likes.state";
import { AuthenticationModel } from 'src/app/states/authentication.state';

@Component({
  selector: 'app-picture-card',
  templateUrl: './picture-card.component.html',
  styleUrls: ['./picture-card.component.sass']
})
export class PictureCardComponent implements OnInit {

  @Input()
  protected picture: PictureResource;

  @Input()
  protected showHeader: boolean = true;

  @Input()
  protected showActions: boolean = true;

  protected comments$: Observable<ImageComment>;

  protected commentsCollapsed = true;

  protected likes: number = 0;

  protected hasLiked: boolean = false;

  protected hasDisliked: boolean = false;

  protected loggedIn: boolean;

  @ViewChild('newComment', { read: ElementRef, static: false })
  private newComment: ElementRef;

  constructor(private store: Store,
    private pictureService: PictureService) {
    store.select(state => state.authentication).subscribe(auth => this.loggedIn = (auth as AuthenticationModel).loggedIn);
  }

  ngOnInit() {
    this.store
      .select(state => state.pictureLikes)
      .subscribe(pictureLikes => {
        if (pictureLikes == undefined || pictureLikes.likes == undefined) {
          return;
        }
        let foundLikes = pictureLikes.likes.find(item => item.imageIdentifier === this.picture.identifier);
        let likes = foundLikes == undefined ? undefined : foundLikes.likes;
        this.likes = likes == undefined ? 0 : likes;
        this.hasLiked = this.loggedIn && foundLikes._links.dislike != undefined;
        this.hasDisliked = this.loggedIn && foundLikes._links.like != undefined;
      });
  }

  getPaddingTop(): string {
    if (!this.showHeader) {
      return "0px";
    } else {
      return "24px";
    }
  }

  getPaddingBottom(): string {
    if (!this.showActions) {
      return "12px";
    } else {
      return "24px";
    }
  }

  profilePictureUrl(): string {
    return `url(${this.picture._embedded.user._links.profilePictureUrl.href})`;
  }

  pictureUrl(): string {
    return `url(${this.picture._links.imageSmall.href})`;
  }

  userDisplayName(): string {
    return this.picture._embedded.user.displayName;
  }

  userIdentifier(): string {
    return this.picture._embedded.user.identifier;
  }

  toggleComments() {
    this.commentsCollapsed = !this.commentsCollapsed;
    if (!this.commentsCollapsed) {
      this.loadComments();
    }
  }

  loadComments() {
    this.store.dispatch(new FindCommentAction(this.picture.identifier)).subscribe(_ =>
      this.store.select<ImageComment>(CommentsState.comments(this.picture.identifier)).subscribe(c => {
        this.comments$ = of(c);
      }));
  }

  addComment() {
    let input = this.newComment.nativeElement.value;
    if (input && input.trim()) {
      this.store.dispatch(new AddCommentAction(this.picture.identifier, input)).subscribe(_ => {
        this.loadComments();
        this.newComment.nativeElement.value = "";
      });
    }
  }

  commentLinkDescription() {
    if (this.commentsCollapsed) {
      return "Show comments";
    }
    return "Hide comments";
  }

  commentButtonDescription() {
    if (this.commentsCollapsed) {
      return "COMMENT";
    }
    return "HIDE COMMENTS";
  }

  downloadPicture() {
    let fileName = 'picture.jpg';
    if (this.picture.title) {
      fileName = this.picture.title.replace(' ', '_').toLowerCase() + '.jpg'
    }
    this.pictureService.downloadImage(this.picture._links.imageRaw.href, fileName);
  }

  likeImage() {
    this.store.dispatch(new LikePictureAction(this.picture.identifier));
  }

  dislikeImage() {
    this.store.dispatch(new DislikePictureAction(this.picture.identifier));
  }
}
