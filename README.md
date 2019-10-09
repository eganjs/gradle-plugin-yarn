# Yarn Plugin

## Mission Statement

This is intended to be an opinionated plugin aimed at delivering a zero configuration experience.

The idea is to provide a plugin that just orchestrates and keeps out of your way as much as possible.

## Requirements

Yarn should already be installed and available on the command line.

The following scripts should be defined in your `package.json`:
- `clean` - links to the `clean` task
- `build:prod` - links to the `assemble` task
- `lint` - links to the `check` task
- `test` - links to the `check` task
