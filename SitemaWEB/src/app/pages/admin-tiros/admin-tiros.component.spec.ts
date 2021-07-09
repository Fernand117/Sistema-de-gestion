import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminTirosComponent } from './admin-tiros.component';

describe('AdminTirosComponent', () => {
  let component: AdminTirosComponent;
  let fixture: ComponentFixture<AdminTirosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminTirosComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminTirosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
