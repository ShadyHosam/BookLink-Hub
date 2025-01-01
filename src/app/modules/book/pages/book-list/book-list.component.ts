import {Component, OnInit} from '@angular/core';
import {BookService} from "../../../../services/services/book.service";
import {Router} from "@angular/router";
import {PageResponseBookResponse} from "../../../../services/models/page-response-book-response";
import {AuthenticationService} from "../../../../services/services/authentication.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit{
  page = 0;
  size = 10;
  bookResponse:PageResponseBookResponse={};


  constructor(private bookService:BookService,
  private router:Router,
              private http : HttpClient,
  ) {

  }
  // wanna display all books to the frontend (user)
    ngOnInit(): void {
       this.findAllBooks();
    }


  private findAllBooks() {

    this.bookService.findAllBooks({
      page:this.page,
      size:this.size,


    }).subscribe({
      next:(books:PageResponseBookResponse)=>{
      this.bookResponse = books;
      }
    });
  }
}

