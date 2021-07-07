import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TirosComponent } from './tiros.component';

describe('TirosComponent', () => {
  let component: TirosComponent;
  let fixture: ComponentFixture<TirosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TirosComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TirosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
