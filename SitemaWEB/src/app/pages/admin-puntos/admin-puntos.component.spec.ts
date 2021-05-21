import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminPuntosComponent } from './admin-puntos.component';

describe('AdminPuntosComponent', () => {
  let component: AdminPuntosComponent;
  let fixture: ComponentFixture<AdminPuntosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminPuntosComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminPuntosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
