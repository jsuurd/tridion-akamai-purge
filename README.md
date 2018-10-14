# Akamai Purge Module for Tridion

Akamai Purge Module for the SDL Web/Tridion Content Deployer micro-service.

## Description

The Tridion Content Deployer can be extended to send purge request for web resources cached by a Content Delivery Network (CDN). This project provides a basic implementation of a deployer module which sends purge request to the [Akamai Content Control Utility API][1]. The implementation can be used as-is or serve as an example how to implement such clients.

## Dependencies

* SDL Web 8.1.0 

This project depends on proprietary libraries that are not publicly available. Use the `Install-LocalDependencies.ps1` script to install these dependencies to the local Maven repository.

	.\Install-LocalDependencies.ps1 -Path <path-to-tridion-jars> 


[1]: https://developer.akamai.com/api/purge/ccu-v2/overview.html
