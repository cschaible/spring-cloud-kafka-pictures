import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {LikesResource} from "../resources/likes/LikesResource";

@Injectable({
  providedIn: 'root'
})
export class LikesService {

  constructor(private httpClient: HttpClient) {
  }

  public likeImages(imageId: string): Observable<LikesResource> {
    return this.httpClient.post<LikesResource>(environment.api + `/images/${imageId}/likes`, {});
  }

  public getLikesForImage(imageId: string): Observable<LikesResource> {
    return this.httpClient.get<LikesResource>(environment.api + `/images/${imageId}/likes`);
  }

}
