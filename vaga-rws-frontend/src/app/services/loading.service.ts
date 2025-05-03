import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class LoadingService {
    private readonly loadingSubject = new BehaviorSubject<boolean>(false);
    public loading$ = this.loadingSubject.asObservable();
    private requestsCount = 0;
    private excludedUrls: string[] = [];

    show() {
        this.requestsCount++;
        // Aguarda 300ms antes de ativar o loader (evita piscadas desnecessárias)
        this.updateLoadingState();
    }

    hide() {
        this.requestsCount = Math.max(0, this.requestsCount - 1);

        setTimeout(() => {
            this.updateLoadingState();
        }, 500);
    }

    private updateLoadingState() {
        this.loadingSubject.next(this.requestsCount > 0);
    }

    setExcludedUrls(urls: string[]) {
        this.excludedUrls = urls;
    }

    isExcluded(url: string): boolean {
        return this.excludedUrls.some(excludedUrl => url.includes(excludedUrl));
    }
}