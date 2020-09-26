import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrls: ['./paginator.component.css']
})
export class PagerComponent implements OnInit {
  @Input() pager: any;
  @Input() callBack: any;
  public disablePrevious = '';
  public disableNext = '';
  public pagesToShow = [];
  public currentPage = 0;
  @Input() pagesNumber = 10;

  constructor() { }

  ngOnInit(): void {
    this.pagesNumber = (this.pager.totalPages < this.pagesNumber) ? this.pager.totalPages : this.pagesNumber;
    this.currentPage = this.pager.number;
    this.updatePager();
  }

  goTo(page: number): void {
    this.callBack(page);
    this.currentPage = page;
    this.updatePager();
  }

  updatePager(): void {
    const pages = [];
    const startOn = (this.currentPage <= this.pagesNumber - 1) ? 0 : this.currentPage - this.pagesNumber + 1;
    for ( let x = startOn; x < this.pagesNumber + startOn; x++){
      pages.push(x);
    }
    this.pagesToShow = pages;
    this.disablePrevious = (this.currentPage === 0) ? 'disabled' : '';
    this.disableNext = (this.currentPage + 1 === (this.pager.totalPages)) ? 'disabled' : '';
  }

}
