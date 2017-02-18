var gulp = require('gulp');
var cleanCSS = require('gulp-clean-css');
var uglify = require('gulp-uglify');

var paths = {
    "css": "src/css/*",
    "data": "src/data/*.json",
    "fonts": "src/fonts/*",
    "images": "src/images/**/*",
    "html": "src/**/*.html",
    "js": "src/scripts/**/*.js",
    "dist": "target/dist/"
};

gulp.task('minify-css', function () {
    return gulp.src(paths.css)
        .pipe(cleanCSS())
        .pipe(gulp.dest(paths.dist + 'css/'));
});

gulp.task('minify-js', function () {
    return gulp.src(paths.js)
        .pipe(uglify())
        .pipe(gulp.dest(paths.dist + 'scripts/'));
});

gulp.task('copy-data', function () {
    return gulp.src(paths.data)
        .pipe(gulp.dest(paths.dist + 'data/'))
});

gulp.task('copy-fonts', function () {
    return gulp.src(paths.fonts)
        .pipe(gulp.dest(paths.dist + 'fonts/'))
});

gulp.task('copy-html', function () {
    return gulp.src(paths.html)
        .pipe(gulp.dest(paths.dist + '/'))
});

gulp.task('copy-images', function () {
    return gulp.src(paths.images)
        .pipe(gulp.dest(paths.dist + 'images/'))
});

gulp.task('default', ['minify-css', 'minify-js', 'copy-data', 'copy-fonts', 'copy-images', 'copy-html'], function () {
});
