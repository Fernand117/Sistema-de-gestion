import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PuntosVentasComponent } from './puntos-ventas.component';

describe('PuntosVentasComponent', () => {
  let component: PuntosVentasComponent;
  let fixture: ComponentFixture<PuntosVentasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PuntosVentasComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PuntosVentasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
