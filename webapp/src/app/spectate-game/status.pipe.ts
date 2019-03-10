import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'status'
})
export class StatusPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if (value == "COMPLETED_WINNER") {
      return "WINNER";
    } else if (value == "COMPLETED_DRAW") {
      return "DRAW";
    } else if (value == "WAITING_FOR_READY") {
      return "NOT STARTED";
    } else if (value == "STARTED") {
      return "IN PROGRESS";
    } else if (value == "STOPPED") {
      return "STOPPED";
    } else if (value == "TIMED_OUT") {
      return "TIMED OUT";
    } else return "UNKNOWN";
  }

}
