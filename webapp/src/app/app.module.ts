import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule, MatSnackBarModule} from '@angular/material';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';

import {NgxsModule} from '@ngxs/store';
import {NgxsStoragePluginModule} from '@ngxs/storage-plugin';
import {NgxsFormPluginModule} from '@ngxs/form-plugin';
import {NgxsLoggerPluginModule} from '@ngxs/logger-plugin';
import {NgxsReduxDevtoolsPluginModule} from '@ngxs/devtools-plugin';

import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {OAuthModule} from 'angular-oauth2-oidc';

import {AppComponent} from './components/app/app.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {AppRoutingModule} from './routing/app-routing.module';
import {OcticonDirective} from './directives/octicon.directive';
import {SearchComponent} from './components/search/search.component';
import {PictureCardComponent} from './components/picture-card/picture-card.component';
import {UserCardComponent} from './components/user-card/user-card.component';
import {UserPicturesComponent} from './components/user-pictures/user-pictures.component';
import {PictureFullsizeComponent} from './components/picture-fullsize/picture-fullsize.component';
import {UserProfileComponent} from './components/user-profile/user-profile.component';
import {RegisterComponent} from './components/register/register.component';
import {RedirectGuard} from './routing/redirect-guard';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CurrentUserState} from './states/current-user.state';
import {AuthenticationState} from './states/authentication.state';
import {CustomHttpInterceptor} from './interceptors/CustomHttpInterceptor';
import {RegisterState} from './states/register.state';
import {PictureSearchState} from './states/picture-search.state';
import {PictureFullsizeState} from './states/picture-fullsize.state';
import {UserSearchState} from './states/user-search.state';
import {UserPictureSearchState} from './states/user-picture-search.state';
import {FileUploadModule} from 'ng2-file-upload';
import {CommentComponent} from './components/comment/comment.component';
import {CommentsState} from './states/comment.state';
import {PictureLikesState} from "./states/picture-likes.state";

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    OcticonDirective,
    SearchComponent,
    PictureCardComponent,
    UserCardComponent,
    UserPicturesComponent,
    PictureFullsizeComponent,
    UserProfileComponent,
    RegisterComponent,
    CommentComponent
  ],
  imports: [
    NgxsModule.forRoot([
      CurrentUserState,
      AuthenticationState,
      RegisterState,
      PictureSearchState,
      PictureFullsizeState,
      UserSearchState,
      UserPictureSearchState,
      CommentsState,
      PictureLikesState
    ]),
    NgxsStoragePluginModule.forRoot(),
    NgxsFormPluginModule.forRoot(),
    NgxsLoggerPluginModule.forRoot({
      logger: console, collapsed: true, disabled: false
    }),
    NgxsReduxDevtoolsPluginModule.forRoot({
      disabled: false
    }),
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule,
    NgbModule,
    FileUploadModule,
    AppRoutingModule,
    OAuthModule.forRoot()
  ],
  providers: [
    RedirectGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: CustomHttpInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
