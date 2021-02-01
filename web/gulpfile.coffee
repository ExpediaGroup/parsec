gulp = require 'gulp'

bower = require 'gulp-bower'
coffee = require 'gulp-coffee'
coffeelint = require 'gulp-coffeelint'
concat = require 'gulp-concat'
concatCss = require 'gulp-concat-css'
del = require 'del'
filter = require 'gulp-filter'
flatten = require 'gulp-flatten'
fs = require('fs')
jade = require 'gulp-jade'
karma = require('karma').Server
mainBowerFiles = require 'main-bower-files'
merge = require 'merge-stream'
ngAnnotate = require 'gulp-ng-annotate'
order = require 'gulp-order'
plumber = require 'gulp-plumber'
sass = require 'gulp-sass'
sequence = require 'gulp-sequence'
uglify = require 'gulp-uglify'
urlAdjuster = require 'gulp-css-url-adjuster'
watch = require 'gulp-watch'

dest = (path) ->
    d = '../resources/public/'
    if path != undefined then d = d + path;
    return d

coffeeOptions =
    bare: true

coffeelintOptions =
    indentation:
        value: 4
    no_trailing_semicolons:
        level: 'ignore'
    no_trailing_whitespace:
        level: 'ignore'
    max_line_length:
        value: 120
        level: 'ignore'

sassOptions =
    outputStyle: 'expanded'

gulp.task 'clean', (done) ->
    del([dest(), 'bower_components'], { force: true }, done)

gulp.task 'bower-install', ->
    bower()

gulp.task 'vendor-styles', ->
    cssFilter = filter '*.css'

    bowerCss = gulp.src mainBowerFiles()
        .pipe cssFilter
        .pipe plumber()
        .pipe flatten()
        .pipe order([
            'normalize.css'
            '**'
        ])
        .pipe urlAdjuster { replace:  [/^\.\/fonts\/.*?\//,'../fonts/'] }
        .pipe urlAdjuster { replace:  [/\.\.\/font\/.*?\//,'../fonts/'] }
        .pipe concat 'vendor.css'
        .pipe gulp.dest dest('css')

gulp.task 'vendor-scripts', ->
    jsFilter = filter '*.js'
    webWorkerFilter = filter 'worker-*.js'

    bowerScripts = gulp.src mainBowerFiles()
        .pipe jsFilter
        .pipe plumber()
        .pipe flatten()
        .pipe order([
            'jquery.js'
            'angular.js'
            'localforage.js'
            '**'
        ])
        .pipe concat 'vendor.js'
        .pipe gulp.dest dest('js')

    webWorkerScripts = gulp.src mainBowerFiles()
        .pipe webWorkerFilter
        .pipe flatten()
        .pipe gulp.dest dest('js')

    merge(bowerScripts, webWorkerScripts)

gulp.task 'vendor-fonts', ->
    fontFilter = filter ['*.eot', '*.woff*', '*.svg', '*.ttf']

    gulp.src mainBowerFiles()
        .pipe fontFilter
        .pipe flatten()
        .pipe gulp.dest dest('fonts')

gulp.task 'vendor-img', ->
    imgFilter = filter ['*.png', '*.gif']

    gulp.src mainBowerFiles()
        .pipe imgFilter
        .pipe flatten()
        .pipe gulp.dest dest('img')

gulp.task 'vendor', sequence('bower-install', ['vendor-fonts', 'vendor-scripts', 'vendor-styles', 'vendor-img'])

# TODO: migrate to Bower
gulp.task 'vendor2', ->
    gulp.src './vendor/**/*.js'
        .pipe flatten()
        .pipe order [
            'ace.js'
            '**'
        ]
        .pipe concat 'vendor2.js'
        .pipe gulp.dest dest('js')

gulp.task 'jade', ->
    gulp.src './app/**/*.jade'
        .pipe plumber()
        .pipe jade {
            pretty: true
        }
        .pipe gulp.dest dest()

gulp.task 'coffee', ->
     gulp.src 'app/**/*.coffee'
        .pipe plumber()
        .pipe order [
            'app/app.coffee'
            '**'
        ]
        .pipe coffee(coffeeOptions)
        .pipe ngAnnotate()
        .pipe concat('app.js')
        .pipe gulp.dest dest('js')

gulp.task 'styles', ->
    gulp.src 'app/styles/*.scss'
    .pipe plumber()
    .pipe sass(sassOptions).on('error', sass.logError)
    .pipe concat('app.css')
    .pipe gulp.dest dest('css')


gulp.task 'assets', ->
    gulp.src ['./app/assets/**/*.*']
        .pipe flatten({ subPath: -1 })
        .pipe gulp.dest dest()

gulp.task 'build', sequence('vendor', ['coffee', 'jade', 'styles', 'assets', 'vendor2'])

gulp.task 'karma', (done) ->
    new karma({
        configFile: __dirname + '/test/karma-unit.conf.coffee'
        singleRun: true
    }, -> done()).start()

gulp.task 'test', sequence('coffee', 'karma')


gulp.task 'watch', ['build'], ->
    watch './app/**/*.{png,jpg}', -> gulp.start ['assets']
    watch './app/**/*.jade', -> gulp.start ['jade']
    watch './app/**/*.scss', -> gulp.start ['styles']
    watch './app/**/*.coffee', -> gulp.start ['coffee']

gulp.task 'default', ['watch']
