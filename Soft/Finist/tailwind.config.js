module.exports = {
  plugins: [require('daisyui')],
  theme: {
    colors: {
      red:    { 100: '#FAF2F4', 200: '#E5DADD', 300: '#521422', 400: '#7A182F', 500: '#F2DADF', 600: '#991F3A' },
      info:   { 100: '#D5DCE5', 200: '#002356', 300: '#003380', 400: '#BFC9D9', 500: '#EDF3FA', 600: '#0045AC' },
      green:  { 100: '#CED9D2', 200: '#073218', 300: '#0B4D25', 400: '#B4CCBD', 500: '#EDFAF3', 600: '#0E6330' },
      yellow: { 100: '#FAF6ED', 200: '#E5DFCF', 300: '#806217', 400: '#D4A326', 500: '#D9D0B8', 600: '#FFC42E' },
      grey:   { 100: '#F5F5F5', 200: '#EDEDED', 300: '#E0E0E0', 400: '#C2C2C2', 500: '#9E9E9E', 600: '#757575', 700: '#616161', 800: '#424242' },
      black:  { 100: '#000000' }
    }
  },
  daisyui: {
    themes: [
      {
        mytheme: {
          "primary": "#991F3A",
          "primary-focus": "#E5DADD",
          "secondary": "#F2DADF",
          "accent": "#7A182F",
          "info": "#0045AC",
          "success": "#0E6330",
          "warning": "#FFC42E",
          "error": "#802323",
        },
      },
    ],
  },
  blocklist: [
    'modal'
  ],
};
