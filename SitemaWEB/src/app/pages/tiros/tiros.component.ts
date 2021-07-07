import { Component, OnInit } from '@angular/core';
import { ApiServiceService } from '../../services/api-service.service';

@Component({
  selector: 'app-tiros',
  templateUrl: './tiros.component.html',
  styleUrls: ['./tiros.component.scss']
})
export class TirosComponent implements OnInit {

  tirosResponse: any;
  itemTiros: any;

  constructor(
    private apiService: ApiServiceService
  ) { }

  ngOnInit(): void {
    this.listaTiros();
  }

  listaTiros() {
    this.apiService.listaTiros().subscribe(
      res => {
        this.tirosResponse = res;
        this.itemTiros = this.tirosResponse['Tiros'];
        console.log(this.itemTiros);
      }
    );
  }
}
