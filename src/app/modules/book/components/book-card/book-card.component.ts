import {Component, EventEmitter, Input, Output} from '@angular/core';
import {PageResponseBookResponse} from "../../../../services/models/page-response-book-response";
import {BookResponse} from "../../../../services/models/book-response";
import {Router} from "@angular/router";

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {


  private _book: BookResponse = {};
  private _manage: boolean = false;
  private _bookCover: string | undefined;

  get manage(): boolean {
    return this._manage;
  }

  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }

  get bookCover(): string | undefined {

    if (this._book.cover) {
      return 'data:image/jpg;base64,' + this._book.cover;
    }
    return 'https://random.imagecdn.app/1000/1000';
  }

  @Output() private share: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();

  @Output() private archive: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();

  @Output() private addToWaitingList: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();

  @Output() private borrow: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private edit: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();

  @Output() private details: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();

  onShowDetails() {
    console.log('onShowDetails');
    return this.details.emit(this._book);

  }

  onBorrow() {
  return this.borrow.emit(this._book);
  }

  onAddToWaitingList() {
    return this.addToWaitingList.emit(this._book);
  }

  onEdit() {
    return this.edit.emit(this._book);
  }

  onShare() {
    return this.share.emit(this._book);
  }

  onArchive() {
    return this.archive.emit(this._book);
  }


  get book(): BookResponse {
    return this._book;
  }

  @Input()
  set book(value: BookResponse) {
    this._book = value;
  }

}
