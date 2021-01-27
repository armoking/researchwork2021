#include <bits/stdc++.h>
#include <iostream>
#include <Windows.h>

#define all(x) begin(x),end(x)
 
using namespace std;
using ll = long long;
 
template<typename T> void read(T& v) { cin >> v; }
template<typename T1, typename T2> void read(pair<T1, T2>& v) { cin >> v.first >> v.second; }
template<typename T> void read(vector<T>& v) { for (auto& val : v) read(val);} 
template<typename T, typename... Args> void read(T& v, Args&... vs) { read(v), read(vs...); }
template<typename T> using min_heap = priority_queue<T, vector<T>, greater<T>>;
template<typename T> using max_heap = priority_queue<T, vector<T>, less<T>>;
 
const int N = 35;
int dp[N][N];
 
int Hemming(string& a, string& b) {
    int n = a.size();
    int ans = 0;
    for (int i = 0; i < n; i++) {
      if (a[i] != b[i]) {
        ans++;
      }
    }
    return ans;
}
 
int Levenshtein(string& a, string& d) {
  int n = a.size();
  int m = d.size();
  for (int i = 0; i <= n; i++) {
    dp[i][0] = i;
  }
  for (int j = 0; j <= m; j++) {
    dp[0][j] = j;
  }
  for (int i = 1; i <= n; i++) {
    for (int j = 1; j <= m; j++) {
      dp[i][j] = min({dp[i - 1][j] + 1, dp[i][j - 1] + 1, dp[i - 1][j - 1] + (a[i - 1] != d[j - 1])});
    }
  }
  return dp[n][m];
}
 
 
int DamerauLevenshtein(string& a, string& d) {
  int n = a.size();
  int m = d.size();
 
  for (int i = 0; i <= n; i++) {
    dp[i][0] = i;
  }
  for (int j = 0; j <= m; j++) {
    dp[0][j] = j;
  }
  for (int i = 1; i <= n; i++) {
    for (int j = 1; j <= m; j++) {
      dp[i][j] = min({dp[i - 1][j] + 1, dp[i][j - 1] + 1, dp[i - 1][j - 1] + (a[i - 1] != d[j - 1])});
      if (i > 1 && j > 1 && a[i - 1] == d[j - 2] && d[j - 1] == a[i - 2]) {
        dp[i][j] = min(dp[i][j], dp[i - 2][j - 2] + 1);
      }
    }
  }
  return dp[n][m];
}

std::vector<std::string> dict[100];


int main() {
  ios_base::sync_with_stdio(false);
  cin.tie(nullptr);
  cout.tie(nullptr);
  setlocale(LC_ALL, "ru");
  SetConsoleCP(1251);// установка кодовой страницы win-cp 1251 в поток ввода
  SetConsoleOutputCP(1251); // установка кодовой страницы win-cp 1251 в поток вывода
  
  string alphabet;
  { 
    
    ifstream is;
    is.open("FuzzyStringSearch\\data_ansi.txt", ios::in);
    string str;
    while (getline(is, str)) {
      dict[str.size()].push_back(str);
    }
    alphabet = str;
    is.close();
  }  
  
  cout << "start reading " << endl;
  cout << alphabet << endl;
  vector<array<double, 51>> arr;
  {
    ifstream is;
    is.open("answer.txt", ios::in);
    int n;
    is >> n;
    arr.resize(n);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < 51; j++) {
        is >> arr[i][j];
      }
    }
    is.close();
  }
  const int sz = alphabet.size();
  assert(sz == 51);
  string current_str = "";
  for (int i = 0; i < int(arr.size()); i++) {
    double val = *max_element(all(arr[i]));
    for (int j = 0; j < 51; j++) {
      if (arr[i][j] == val) {
        current_str.push_back(alphabet[j]);
        break;
      }
    }
  }
  
  current_str.pop_back();
  cout << current_str << endl;
 
  int mx = 1000;
  int len = arr.size();
  string answer = current_str;
  for (int i = max(1, len - 7); i <= min(35, len + 7); i++) {
    for (auto& str : dict[i]) {
      int cur = DamerauLevenshtein(current_str, str);
      if (cur < mx) {
        mx = cur;
        answer = str;
        cout << mx << ' ' << answer << endl;
      }
    }
  }

  freopen("imp_answer.txt", "w", stdout);
  cout << answer << endl;
  
}
