import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { OAuthService } from "angular-oauth2-oidc";
import { Observable, of } from "rxjs";

import { environment } from "../../environments/environment";
import { catchError } from "rxjs/operators";
import { MatSnackBar } from "@angular/material";

@Injectable()
export class CustomHttpInterceptor implements HttpInterceptor {
  constructor(private oauthService : OAuthService, private snackBar: MatSnackBar) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let token = this.oauthService.getAccessToken();
    if(token && req.url.indexOf(environment.api) >= 0) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(req).pipe(
      catchError(error => this.handleError(error))
    );
  }

  handleError(error) {
    this.snackBar.open(error.message, undefined, { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
    return of(error);
  }
}
