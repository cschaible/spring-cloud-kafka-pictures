import { Component, OnInit, Input, ViewChild, ElementRef } from '@angular/core';
import { PictureResource } from '../../resources/picture/PictureResource';
import { Store, Select } from '@ngxs/store';
import { CommentResource } from '../../resources/comment/CommentResource';
import { CommentsState, FindCommentAction, ImageComment, AddCommentAction } from '../../states/comment.state';
import { Observable, of } from 'rxjs';
import { PictureService } from '../../services/picture.service';
import {LikePictureAction} from "../../states/picture-likes.state";

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

  @ViewChild('newComment', { read: ElementRef, static: false }) 
  private newComment: ElementRef;

  constructor(private store: Store, private pictureService: PictureService) { }

  ngOnInit() {
    this.store
      .select(state => state.pictureLikes)
      .subscribe(pictureLikes => {
        let likes = pictureLikes.likes.find(item => item.imageIdentifier === this.picture.identifier).likes;
        this.likes = likes == undefined ? 0 : likes
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
}
