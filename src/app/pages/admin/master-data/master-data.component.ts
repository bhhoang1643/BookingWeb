import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { ChartConfiguration } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';  // ✅ dùng BaseChartDirective
import { NgChartsModule } from 'ng2-charts';

@Component({
  selector: 'app-master-data',
  standalone: true,
  imports: [CommonModule, NgChartsModule],
  templateUrl: './master-data.component.html',
  styleUrls: ['./master-data.component.css']
})
export class MasterDataComponent implements OnInit {

  @ViewChild(BaseChartDirective) chart?: BaseChartDirective; // ✅ thêm ViewChild để update chart

  dashboardData: any = {};
  monthlyRevenueLabels: string[] = [];
  monthlyRevenueData: number[] = [];

  chartConfig: ChartConfiguration<'line'> = {
    type: 'line',
    data: {
      labels: [],
      datasets: [
        {
          label: 'Monthly Revenue (VNĐ)',
          data: [],
          borderColor: '#4CAF50',
          backgroundColor: 'rgba(76, 175, 80, 0.2)',
          fill: true,
          tension: 0.4
        }
      ]
    },
    options: {
      responsive: true,
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            callback: function (value) {
              return value.toLocaleString('vi-VN'); // ✅ format số tiền đẹp
            }
          }
        }
      },
      plugins: {
        legend: {
          display: true,
          labels: {
            font: {
              size: 14
            }
          }
        }
      }
    }
  };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadDashboardSummary();
    this.loadMonthlyRevenue();
  }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  private loadDashboardSummary() {
    this.http.get<any>('http://localhost:8080/api/admin-dashboard/summary', { headers: this.getAuthHeaders() })
      .subscribe({
        next: (data) => {
          console.log('✅ Dashboard summary loaded:', data);
          this.dashboardData = data;
        },
        error: (err) => {
          console.error('❌ Failed to load dashboard summary:', err);
        }
      });
  }

  private loadMonthlyRevenue() {
    this.http.get<{ [month: string]: number }>('http://localhost:8080/api/admin-dashboard/monthly-revenue', { headers: this.getAuthHeaders() })
      .subscribe({
        next: (data) => {
          console.log('✅ Monthly revenue loaded:', data);

          this.monthlyRevenueLabels = Object.keys(data).map(month => this.mapMonthNumberToName(month));
          this.monthlyRevenueData = Object.values(data);

          // ✅ Cập nhật chart
          this.chartConfig.data.labels = this.monthlyRevenueLabels;
          this.chartConfig.data.datasets[0].data = this.monthlyRevenueData;

          // ✅ Force ChartJS vẽ lại ngay lập tức
          this.chart?.update();
        },
        error: (err) => {
          console.error('❌ Failed to load monthly revenue:', err);
        }
      });
  }

  private mapMonthNumberToName(month: string): string {
    const months = [
      'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
      'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'
    ];
    return months[parseInt(month, 10) - 1] || '';
  }
}
